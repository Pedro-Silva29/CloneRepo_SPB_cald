package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.food.Food;
import com.example.demo.food.FoodRepository;
import com.example.demo.food.FoodRequestDTO;
import com.example.demo.food.FoodResponseDTO;

@RestController
@RequestMapping("food")
public class FoodController {

    @Autowired
    private FoodRepository repository;

    // Renomeado para evitar conflito
    @DeleteMapping("{id}")
    public void deleteFood(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PostMapping
    public void saveFood(@RequestBody FoodRequestDTO data) {
        Food foodData = new Food(data);
        repository.save(foodData);
    }

    @PutMapping("{id}")
    public FoodResponseDTO updateFood(@PathVariable Long id, @RequestBody FoodRequestDTO data) {
        Food food = repository.findById(id).orElseThrow(() -> new RuntimeException("Food not found"));
        food.setTitle(data.title());
        food.setImage(data.image());
        food.setPrice(data.price());
        repository.save(food);
        return new FoodResponseDTO(food);
    }

    @PatchMapping("{id}")
    public FoodResponseDTO patchFood(@PathVariable Long id, @RequestBody FoodRequestDTO data) {
        // Busca o alimento pelo ID informado
        Food food = repository.findById(id).orElseThrow(() -> new RuntimeException("Food not found"));

        // Atualiza os campos do alimento com os novos valores enviados, se não forem nulos
        if (data.title() != null) {
            food.setTitle(data.title());
        }
        if (data.image() != null) {
            food.setImage(data.image());
        }
        if (data.price() != null) {
            food.setPrice(data.price());
        }

        // Salva as mudanças no banco de dados
        repository.save(food);

        // Retorna a versão atualizada do alimento
        return new FoodResponseDTO(food);
    }

    @GetMapping
    public List<FoodResponseDTO> getAll() {
        List<FoodResponseDTO> foodList = repository.findAll().stream()
            .map(FoodResponseDTO::new)
            .collect(Collectors.toList());
        return foodList;
    }
}
