package co.edu.uniandes.dse.parcial1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.EstadioEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstadioRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstadioService {

    // Complete

    @Autowired //Necesario para que esta variable/dependencia sea inyectada
    EstadioRepository estadioRepository;

    @Transactional //Si algo pasa mal, te restituye la base de datos en el estado anterior que estaba
    public EstadioEntity createEstadio(EstadioEntity estadio) throws IllegalOperationException //Vamos a disparar un exception si se rompe una regla de negocio
    {
        if(estadio.getCiudad().length() < 3) {
            throw new IllegalOperationException("El nombre de la ciudad debe tener más de 3 letras");
        }

        if (estadio.getAforo() < 1000 || estadio.getAforo() > 1000000)
			throw new IllegalOperationException("Aforo invalido");

        if (estadio.getPrecioAlquiler() < 100000)
			throw new IllegalOperationException("Precio alquiler invalido");


        return estadioRepository.save(estadio); 
    }
}

