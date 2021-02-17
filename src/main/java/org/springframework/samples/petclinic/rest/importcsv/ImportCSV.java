package org.springframework.samples.petclinic.rest.importcsv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.AssertFalse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api/import")
public class ImportCSV {

    @Autowired
    private ClinicService clinicService;

    @PreAuthorize("hasRole(@roles.OWNER_ADMIN)")
    @RequestMapping(value = "importPets",
        method = RequestMethod.POST,
        consumes = "text/plain",
        produces = "application/json")
    public ResponseEntity<List<Pet>> importPets(@RequestBody String csv) {

        int i = 0;
        List<Pet> pets = new LinkedList<Pet>();
        Pet pet;

        do {
            pet = new Pet();

            String petname = "";
            while (i < csv.length() && csv.charAt(i) != ';') {
                petname += csv.charAt(i++);
            }
            i++;

            pet.setName(petname);

            String birthdate = "";
            while (i < csv.length() && csv.charAt(i) != ';') {
                birthdate += csv.charAt(i++);
            }
            i++;

            try {
                pet.setBirthDate((new SimpleDateFormat("yyyy-MM-dd")).parse(birthdate));
            } catch (ParseException e) {
                HttpHeaders headers = new HttpHeaders();
                headers.add("errors", "date " + birthdate + " not valid");
                return new ResponseEntity<List<Pet>>(headers, HttpStatus.BAD_REQUEST);
            }

            String pettype = "";
            while (i < csv.length() && csv.charAt(i) != ';') {
                pettype += csv.charAt(i++);
            }
            i++;

            if (pet != null) {
                ArrayList<PetType> ts = (ArrayList<PetType>) clinicService.findPetTypes();
                for (int j = 0; j < ts.size(); j++) {
                    if (ts.get(j).getName().toLowerCase().equals(pettype)) {

                        pet.setType( ts.get(j));
                    }
                }

            }

            String tmpowner = "";
            while (i < csv.length() && (csv.charAt(i) != ';' && csv.charAt(i) != '\n')) {
                tmpowner += csv.charAt(i++);
            }

            if (pet != null) {
                String owner = tmpowner;
                List<Owner> matchingOwners = clinicService.findAllOwners()
                    .stream()
                    .filter(o -> o.getLastName().equals(owner))
                    .collect(Collectors.toList());

                if (matchingOwners.size() == 0) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("errors", "Owner not found");
                    return new ResponseEntity<List<Pet>>(headers, HttpStatus.BAD_REQUEST);
                }
                if (matchingOwners.size() > 1) {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("errors", "Owner not unique");
                    return new ResponseEntity<List<Pet>>(headers, HttpStatus.BAD_REQUEST);
                }
                pet.setOwner(matchingOwners.iterator().next());
            }

            if (csv.charAt(i) == ';') {
                i++;

                String action = "";
                while (i < csv.length() && csv.charAt(i) != '\n') {
                    action += csv.charAt(i++);
                }

                if (action.toLowerCase().equals("add")) {
                    clinicService.savePet(pet);
                } else {
                    for (Pet q : pet.getOwner().getPets()) {
                        if (q.getName().equals(pet.getName())) {
                            if (q.getType().getId().equals(pet.getType().getId())) {
                                if (pet.getBirthDate().equals(q.getBirthDate())) {
                                    clinicService.deletePet(q);
                                }
                            }
                        }
                    }
                }

            } else {
                clinicService.savePet(pet);
            }
            i++;

            pets.add(pet);

        } while (i < csv.length() && pet != null);

        return new ResponseEntity<List<Pet>>(pets, HttpStatus.OK);
    }


}
