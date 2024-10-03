package graph;

import java.util.HashMap;
import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.Trancon;

public class MonTroncon implements Trancon {
    private Lieu depart;
    private Lieu arrive;
    private ModaliteTransport modaliteTransport;
    private HashMap<TypeCout, Double> cout;

    public MonTroncon(Lieu depart, Lieu arrive, ModaliteTransport md, HashMap<TypeCout, Double> cout){
        this.depart = depart;
        this.arrive = arrive;
        this.modaliteTransport = md;
        this.cout = cout;
    }
    
    @Override
    public Lieu getDepart() {
        return this.depart;
    }

    @Override
    public Lieu getArrivee() {
        return this.arrive;
    }

    @Override
    public ModaliteTransport getModalite() {
        return this.modaliteTransport;
    }

    public HashMap<TypeCout, Double> getCout() {
        return cout;
    }

    public void setCout(HashMap<TypeCout, Double> cout) {
        this.cout = cout;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MonTroncon other = (MonTroncon) obj;
        if (depart == null) {
            if (other.depart != null)
                return false;
        } else if (!depart.equals(other.depart))
            return false;
        if (arrive == null) {
            if (other.arrive != null)
                return false;
        } else if (!arrive.equals(other.arrive))
            return false;
        if (modaliteTransport != other.modaliteTransport)
            return false;
        if (cout == null) {
            if (other.cout != null)
                return false;
        } else if (!cout.equals(other.cout))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MonTroncon [depart=" + depart + ", arrive=" + arrive + ", modaliteTransport=" + modaliteTransport
                + ", cout=" + cout + "]";
    }
}
