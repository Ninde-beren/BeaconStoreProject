package imie.angers.fr.beaconstoreproject.metiers;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import java.lang.String;

import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;

/**
 * Permet de créer un objet NotificationMétier et d'accéder à ses attributs
 * Implémente l'interface Parcelable permettant de passer des instances d'objets entre activités
 * Created by Anne on 13/02/2016.
 */

public class NotificationMetier implements Parcelable {

    //Attributs de notre objet NotificationMetier
    private int id_notif;
    private String titrePromo;
    private String lbPromo;
    private String imageoff;
    private String idBeacon;
    private int idPromo;
    private String dateAjoutNotif;

    public NotificationMetier() {}

    //Getters & Setters
    public int getId() {
        return id_notif;
    }

    public void setId(int id) {
        this.id_notif = id;
    }

    public String getTitrePromo() {
        return titrePromo;
    }

    public void setTitrePromo(String titrePromo) {
        this.titrePromo = titrePromo;
    }

    public String getLbPromo() {
        return lbPromo;
    }

    public void setLbPromo(String lbPromo) {
        this.lbPromo = lbPromo;
    }

    public String getImageoff() {
        return imageoff;
    }

    public void setImageoff(String imageoff) {
        this.imageoff = imageoff;
    }

    public int getId_notif() {
        return id_notif;
    }

    public void setId_notif(int id_notif) {
        this.id_notif = id_notif;
    }

    public String getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(String idBeacon) {
        this.idBeacon = idBeacon;
    }

    public int getIdPromo() {
        return idPromo;
    }

    public void setIdPromo(int idPromo) {
        this.idPromo = idPromo;
    }

    public String getDateAjoutNotif() {
        return dateAjoutNotif;
    }

    public void setDateAjoutNotif(String dateAjoutNotif) {
        this.dateAjoutNotif = dateAjoutNotif;
    }

    //Méthodes de l'interface Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_notif);
        dest.writeString(titrePromo);
        dest.writeString(lbPromo);
        dest.writeString(imageoff);
        dest.writeString(idBeacon);
        dest.writeInt(idPromo);
        dest.writeString(dateAjoutNotif);
    }


    protected NotificationMetier(Parcel in) {
        id_notif = in.readInt();
        titrePromo = in.readString();
        lbPromo = in.readString();
        imageoff = in.readString();
        idBeacon = in.readString();
        idPromo = in.readInt();
        dateAjoutNotif = in.readString();
    }

    public static final Creator<NotificationMetier> CREATOR = new Creator<NotificationMetier>() {
        @Override
        public NotificationMetier createFromParcel(Parcel in) {
            return new NotificationMetier(in);
        }

        @Override
        public NotificationMetier[] newArray(int size) {
            return new NotificationMetier[size];
        }
    };

}
