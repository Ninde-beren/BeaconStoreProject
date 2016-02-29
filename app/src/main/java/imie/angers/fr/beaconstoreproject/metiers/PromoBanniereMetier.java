package imie.angers.fr.beaconstoreproject.metiers;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Permet de créer un objet PromoBanniereMetier et d'accéder à ses attributs
 * Hérite de la classe Notification
 * Implémente l'interface Parcelable permettant de passer des instances d'objets entre activités
 * Created by Ninde on 22/02/2016.
 */
public class PromoBanniereMetier extends NotificationMetier {

    //Attributs de notre objet NotificationMetier

    private int id_Banniere;
    private String idBanniere;
    private String txtBanniere;
    private String dtdebval;
    private String dtfinval;
    private String typBanniere;
    private String imageart;

    public PromoBanniereMetier() {
        super();
    }

    //Getters & Setters

    public String getIdbanniere() {
        return idBanniere;
    }

    public void setIdbanniere(String idbanniere) {
        this.idBanniere = idbanniere;
    }

    public String getTxtBanniere() {
        return txtBanniere;
    }

    public void setTxtBanniere(String txtBanniere) { this.txtBanniere = txtBanniere; }

    public String getTypBanniere() {
        return typBanniere;
    }

    public void setTypBanniere(String typBanniere) {
        this.typBanniere = typBanniere;
    }

    public String getImageart() {
        return imageart;
    }

    public void setImageart(String imageart) {
        this.imageart = imageart;
    }

    public String getDtfinval() {
        return dtfinval;
    }

    public void setDtfinval(String dtfinval) {
        this.dtfinval = dtfinval;
    }

    public String getDtdebval() {
        return dtdebval;
    }

    public void setDtdebval(String dtdebval) {
        this.dtdebval = dtdebval;
    }

    public int getId_Banniere() {
        return id_Banniere;
    }

    public void setId_Banniere(int id_Banniere) {
        this.id_Banniere = id_Banniere;
    }

    //Méthodes de l'interface Parcelable

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);

        dest.writeInt(this.id_Banniere);
        dest.writeString(this.idBanniere);
        dest.writeString(this.txtBanniere);
        dest.writeString(this.dtdebval);
        dest.writeString(this.dtfinval);
        dest.writeString(this.imageart);
        dest.writeString(this.typBanniere);
    }
    
    protected PromoBanniereMetier(Parcel in) {
        super(in);

        this.id_Banniere = in.readInt();
        this.idBanniere = in.readString();
        this.txtBanniere = in.readString();
        this.dtdebval = in.readString();
        this.dtfinval = in.readString();
        this.imageart = in.readString();
        this.typBanniere = in.readString();
    }

    public static final Parcelable.Creator<PromoBanniereMetier> CREATOR = new Parcelable.Creator<PromoBanniereMetier>() {
        @Override
        public PromoBanniereMetier createFromParcel(Parcel in) {
            return new PromoBanniereMetier(in);
        }

        @Override
        public PromoBanniereMetier[] newArray(int size) {
            return new PromoBanniereMetier[size];
        }
    };

}