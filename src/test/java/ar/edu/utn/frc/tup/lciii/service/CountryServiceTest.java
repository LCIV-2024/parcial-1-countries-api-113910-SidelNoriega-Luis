package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CountryServiceTest {

    @Autowired
    CountryService countryService;

    @Test
    void getAllCountries() {
        List<Country> countries = countryService.getAllCountries();
        assertNotEquals(0, countries.size());
    }

    @Test
    void getAllCountriesDTO() {
        List<CountryDTO> countries = countryService.getAllCountriesDTO(null, null);
        assertNotEquals(0, countries.size());
    }

    @Test
    void getCountriesByContinent() {
        List<CountryDTO> response = countryService.getCountriesByContinent("Americas");
        assertEquals(56, response.size());
    }

    @Test
    void getCountriesByLanguaje() {
        List<CountryDTO> response = countryService.getCountriesByLanguaje("Spanish");
        assertEquals(24, response.size());
    }

    @Test
    void getCountryWithMostBorders() {
        CountryDTO response = countryService.getCountryWithMostBorders();
        assertEquals("China", response.getName());
    }

    @Test
    void saveCountries() {
        List<CountryDTO> response = countryService.saveCountries(10);
        assertEquals(10, response.size());
    }
}