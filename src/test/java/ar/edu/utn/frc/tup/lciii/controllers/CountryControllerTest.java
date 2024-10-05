package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.SaveResquest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CountryControllerTest {
    @Autowired
    CountryController countryController;


    @Test
    void getAllCountries() {
        ResponseEntity<List<CountryDTO>> response =countryController.getAllCountries(null, null);
        assertNotEquals(0,response.getBody().size());
    }

    @Test
    void getCountriesByContinent() {
        ResponseEntity<List<CountryDTO>> response =countryController.getCountriesByContinent("Americas");
        assertEquals(56,response.getBody().size());
        assertNotEquals(0,response.getBody().size());
    }

    @Test
    void getCountriesByLanguage() {
        ResponseEntity<List<CountryDTO>> response =countryController.getCountriesByLanguage("Spanish");
        assertEquals(24,response.getBody().size());
        assertNotEquals(0,response.getBody().size());
    }

    @Test
    void getCountryWithMostBorders() {
        ResponseEntity<CountryDTO> response =countryController.getCountryWithMostBorders();
        assertEquals("China",response.getBody().getName());
    }

    @Test
    void saveCountries() {
        ResponseEntity<List<CountryDTO>> response =countryController.saveCountries(new SaveResquest(10));
        assertEquals(10,response.getBody().size());

    }
}