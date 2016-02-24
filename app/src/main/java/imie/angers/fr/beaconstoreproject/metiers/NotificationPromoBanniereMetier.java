package imie.angers.fr.beaconstoreproject.metiers;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Permet de créer un objet NotificationPromoBanniereMetier et d'accéder à ses attributs
 * Implémente l'interface Parcelable permettant de passer des instances d'objets entre activités
 * Created by Ninde on 22/02/2016.
 */
public class NotificationPromoBanniereMetier implements Parcelable {

    //Attributs de notre objet NotificationMetier
    private int id_notif;
    private String titreBanniere;
    private String lbBanniere;
    private String imageoff;
    private int idBanniere;

    public NotificationPromoBanniereMetier() {}

    //Getters & Setters

    public int getId() {
        return id_notif;
    }

    public void setId(int id) {
        this.id_notif = id;
    }

    public String getTitreBanniere() {
        return titreBanniere;
    }

    public void setTitreBanniere(String titreBanniere) {
        this.titreBanniere = titreBanniere;
    }

    public String getLbBanniere() { return lbBanniere; }

    public void setLbBanniere(String lbBanniere) { this.lbBanniere = lbBanniere; }

    public String getImageoff() {
        return imageoff;
    }

    public void setImageoff(String imageoff) {
        this.imageoff = imageoff;
    }

    public int getIdBanniere() { return idBanniere; }

    public void setIdBanniere(int idBanniere) { this.idBanniere = idBanniere; }

    //Méthodes de l'interface Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_notif);
        dest.writeString(titreBanniere);
        dest.writeString(lbBanniere);
        dest.writeString(imageoff);
        dest.writeInt(idBanniere);
    }


    protected NotificationPromoBanniereMetier(Parcel in) {
        id_notif = in.readInt();
        titreBanniere = in.readString();
        lbBanniere = in.readString();
        imageoff = in.readString();
        idBanniere = in.readInt();
    }

    public static final Parcelable.Creator<NotificationPromoBanniereMetier> CREATOR = new Parcelable.Creator<NotificationPromoBanniereMetier>() {
        @Override
        public NotificationPromoBanniereMetier createFromParcel(Parcel in) { return new NotificationPromoBanniereMetier(in); }

        @Override
        public NotificationPromoBanniereMetier[] newArray(int size) { return new NotificationPromoBanniereMetier[size]; }
    };

}