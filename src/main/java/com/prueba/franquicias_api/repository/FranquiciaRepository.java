package com.prueba.franquicias_api.repository;


import com.prueba.franquicias_api.model.Franquicia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranquiciaRepository extends MongoRepository<Franquicia, String> {
}