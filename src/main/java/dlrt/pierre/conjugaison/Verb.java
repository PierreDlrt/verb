package dlrt.pierre.conjugaison;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
public class Verb implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int _id;

    @ColumnInfo(name = "verb")
    private String verb;

    @ColumnInfo(name = "present")
    private VxT present;

    @ColumnInfo(name = "imparfait")
    private VxT imparfait;

    @ColumnInfo(name = "futur")
    private VxT futur;

    @ColumnInfo(name = "passe_simple")
    private VxT passe_simple;

    @ColumnInfo(name = "imperatif")
    private VxT imperatif;

    @ColumnInfo(name = "cond_pres")
    private VxT cond_pres;

    @ColumnInfo(name = "sub_pres")
    private VxT sub_pres;

    @ColumnInfo(name = "sub_imp")
    private VxT sub_imp;

    @ColumnInfo(name = "gerondif")
    private VxT gerondif;

    @ColumnInfo(name = "part_passe")
    private VxT part_passe;

    @ColumnInfo(name = "fil")
    private int fil;


    /*
    * Getters and Setters
    * */

    public int get_id() { return _id; }
    public void set_id(int _id) {
        this._id = _id;
    }

    public String getVerb() {
        return verb;
    }
    public void setVerb(String verb) {
        this.verb = verb;
    }

    public VxT getPresent() { return present; }
    public void setPresent(VxT present) {
        this.present = present;
    }

    public VxT getImparfait() { return imparfait; }
    public void setImparfait(VxT imparfait) {
        this.imparfait = imparfait;
    }

    public VxT getFutur() { return futur; }
    public void setFutur(VxT futur) {
        this.futur = futur;
    }

    public VxT getPasse_simple() { return passe_simple; }
    public void setPasse_simple(VxT passe_simple) {
        this.passe_simple = passe_simple;
    }

    public VxT getImperatif() { return imperatif; }
    public void setImperatif   (VxT imperatif) { this.imperatif = imperatif; }

    public VxT getCond_pres() { return cond_pres; }
    public void setCond_pres(VxT cond_pres) {
        this.cond_pres = cond_pres;
    }

    public VxT getSub_pres() { return sub_pres; }
    public void setSub_pres(VxT sub_pres) {
        this.sub_pres = sub_pres;
    }

    public VxT getSub_imp() { return sub_imp; }
    public void setSub_imp(VxT sub_imp) {
        this.sub_imp = sub_imp;
    }

    public VxT getGerondif() { return gerondif; }
    public void setGerondif(VxT gerondif) {
        this.gerondif = gerondif;
    }

    public VxT getPart_passe() { return part_passe; }
    public void setPart_passe(VxT part_passe) {
        this.part_passe = part_passe;
    }

    public int getFil() { return fil; }
    public void setFil(int fil) {
        this.fil = fil;
    }

    public VxT getEmpty() {return new VxT("", "", "", "", "", "");}

    public VxT getTemps(int selTps) {
        switch (selTps) {
            case 0: return present;
            case 1: return imparfait;
            case 2: return futur;
            case 3: return passe_simple;
            case 4: return imperatif;
            case 5: return cond_pres;
            case 6: return sub_pres;
            case 7: return sub_imp;
            case 8: return gerondif;
            case 9: return part_passe;
            default: return new VxT("", "", "", "", "", "");
        }
    }

    public void setTemps(int pos, VxT vxT) {
        switch (pos) {
            case 0: this.present = vxT; break;
            case 1: this.imparfait = vxT; break;
            case 2: this.futur = vxT; break;
            case 3: this.passe_simple = vxT; break;
            case 4: this.imperatif = vxT; break;
            case 5: this.cond_pres = vxT; break;
            case 6: this.sub_pres = vxT; break;
            case 7: this.sub_imp = vxT; break;
            case 8: this.gerondif = vxT; break;
            case 9: this.part_passe = vxT; break;
            default: break;
        }
    }

    public String verbToString() {
        return this.verb;
    }

    public int getFilledCount() {
        AtomicInteger cpt = new AtomicInteger(10);
        for (int i = 0;i <= 9;i++){
            if (getTemps(i).getIo().equals("")) { cpt.getAndDecrement(); }
        }
        return cpt.get();
    }

    public String dispTps(int t) {
        switch (t) {
            case 0: return "Présent";
            case 1: return "Imparfait";
            case 2: return "Futur";
            case 3: return "Passé simple";
            case 4: return "Imperatif";
            case 5: return "Conditionnel présent";
            case 6: return "Subjonctif présent";
            case 7: return "Subjonctif présent";
            case 8: return "Gérondif";
            case 9: return "Participe passé";
            default: return "";
        }
    }
}
