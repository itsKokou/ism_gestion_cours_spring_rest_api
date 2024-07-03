package projet.restapi.service;

import projet.core.data.entities.Seance;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class Disponibilite {

    public static Boolean isDisponible(List<Seance>seances, Date date, LocalTime heureD, LocalTime heureF){
        for (Seance seance : seances){
            if ( seance.getDate().compareTo(date) ==0 ){
                if ( seance.getHeureD().isBefore(heureF) && heureF.isBefore(seance.getHeureD())){
                    return false;
                } else if (seance.getHeureD().isBefore(heureD) && heureD.isBefore(seance.getHeureD()) ) {
                    return false;
                }else if( (heureD.isBefore(seance.getHeureD()) || heureD.equals(seance.getHeureD())) && (seance.getHeureF().isBefore(heureF) || seance.getHeureF().equals(heureF)) ){
                    return false;
                } else if ((seance.getHeureD().isBefore(heureD)|| seance.getHeureD().equals(heureD)) && (heureF.isBefore(seance.getHeureF()) || heureF.equals(seance.getHeureF())) ) {
                    return false;
                }
            }
        }
        return true;
    }
}
