package com.ucr.reco.service;


import com.ucr.reco.model.Space;
import com.ucr.reco.repository.SpaceJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {

    @Autowired
    private SpaceJpaRepository repository;

    //Metodo que lista los objetos de la clase space
    public List<Space> findAll()
    {
        return repository.findAll();
    }

    //Metodo que obtiene por id
    public Space getById(Integer id)
    {
        return repository.getById(id);
    }

    //Metodo que crea un nuevo espacio
    public Space add(Space space)
    {
        //Crea un objeto tipo space donde hereda o guarda la informacion del metodo getByName en el repositorio
        Space spaceExits = repository.getByName(space.getName());
        if (spaceExits != null)//Valida si el nombre existe
        {
            return null;//Si es así, no lo agrega
        } else
        {
            if (space.getName() == null || space.getLocation() == null || space.getType() == null || space.getPrice() == null) {
                return null;//Si algún campo requerido es nulo, no lo agrega
            }
        }
        return repository.save(space);//Guarda y retorna el nuevo space
    }//Fin del metodo

    public Space update(Space space)
    {
        //Crea un objeto tipo space donde guarda la información del metodo que encuenta por id
        Space spaceExits = repository.getById(space.getId());
        if (spaceExits != null) //Si la variable local contiene información
        {
            if (space.getName() != null) //Revisa si el usuario envía el nombre
            {
                spaceExits.setName(space.getName());//Si lo hay, actualiza el nombre
            }
            if (space.getLocation() != null)//Revisa si el usuario envía la ubicación
            {
                spaceExits.setLocation(space.getLocation());//Si lo hace, actualiza la ubicación
            }
            if (space.getType() != null)//Revisa si el usuario envía un tipo
            {
                spaceExits.setType(space.getType());//Si lo hace, actualiza el tipo
            }
            if (space.getPrice() != null)//Revisa si el usuario envía un precio
            {
                spaceExits.setPrice(space.getPrice());//Si lo hace, lo actualiza
            }

        } else//Si no pasa nada de eso
        {
            return null;//Retorna null
        }
        return repository.save(spaceExits);//Guarda la información ingresada
    }

    public Space delete(Integer id)
    {
        Space spaceExits = repository.getById(id);//Se crea un objeto tipo space que guarda los resultados del metodo buscar por id
        if (spaceExits != null) //Si no está vacío
        {
            repository.deleteById(id);//Elimina el id
            return spaceExits;//Retorna el espacio eliminado
        }
        return null;//Si no hay nada, retorna null
    }


    public Space changePrice(Integer id, Double price)
    {
        Optional<Space> spaceExits = repository.findById(id);//Se crea un objeto tipo space que guarda los resultados del metodo buscar por id
        if (spaceExits.isPresent()) //Revisa si hay información
        {
            Space spaceTemp = spaceExits.get();
            spaceTemp.setPrice(price);
            return repository.save(spaceTemp);
        }
        return null;
    }

}
