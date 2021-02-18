package br.com.monkey.ecx.person;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final List<Person> PEOPLE = new ArrayList<>(Arrays.asList(new Person("NAME1"), new Person("NAME2"), new Person("NAME3")));

    @PostMapping
    @Operation(operationId = "person/PersonPost.json")
    public Person create(@RequestBody Person person) {
        PEOPLE.add(person);
        return person;
    }

    @GetMapping
    @Operation(operationId = "person/PersonGETALL.json")
    public List<Person> getNames() {
        return PEOPLE;
    }

    @GetMapping(value = "/{name}")
    @Operation(operationId = "person/PersonGET.json")
    public Person getName(@PathVariable("name") String name) {
        return PEOPLE.stream().filter(n -> n.getName().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Come on use the corret HTTP verb for this :("));
    }

}