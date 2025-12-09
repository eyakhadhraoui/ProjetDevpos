package tn.esprit.tpfoyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.repository.ChambreRepository;

import java.util.List;

@Service
public class blocService {
    @Autowired
    ChambreRepository chambreRepository;
    @Autowired
    BlocRepository blocRepository;
    List<Bloc> retrieveBlocs(){
        return blocRepository.findAll();
    }
    Bloc updateBloc (Bloc  bloc){
        return blocRepository.save(bloc);
    }

    public Bloc addBloc (Bloc bloc){
        return blocRepository.save(bloc);
    }
    Bloc retrieveBloc (long  idBloc){
        return  blocRepository.findById(idBloc).get();
    }
    void removeBloc (long idBloc){
        blocRepository.deleteById(idBloc);
    }

    public Bloc affecterChambresABloc(List<Long> numChambre, long idBloc) {
        Bloc bloc = blocRepository.findById(idBloc).get();
        List<Chambre> chambres = chambreRepository.findAllByNumeroChambreIn(numChambre);
        bloc.setChambres(chambres);
        blocRepository.save(bloc);
        return bloc;
    }


}
