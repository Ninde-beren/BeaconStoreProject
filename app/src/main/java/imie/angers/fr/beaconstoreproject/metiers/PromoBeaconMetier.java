package imie.angers.fr.beaconstoreproject.metiers;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import imie.angers.fr.beaconstoreproject.utils.BitMapUtil;

/**
 * Permet de créer un objet PromoBeaconMetier et d'accéder à ses attributs
 * Hérite de la classe Notification
 * Implémente l'interface Parcelable permettant de passer des instances d'objets entre activités
 * Created by Anne on 17/02/2016.
 */

public class PromoBeaconMetier extends NotificationMetier {

    //Attributs de notre objet PromoBeaconMetier
    private int id_promo;
    private String idpromo;
    private String txtPromo;
    private String typpromo;
    private String imageart;
    private String idmagasin;
    private String dateAjoutPromo;

    public PromoBeaconMetier() {
        super();
    }

    //Getters & Setters


    public String getIdmagasin() {
        return idmagasin;
    }

    public void setIdmagasin(String idmagasin) {
        this.idmagasin = idmagasin;
    }

    public String getTxtPromo() {
        return txtPromo;
    }

    public void setTxtPromo(String txtPromo) {
        this.txtPromo = txtPromo;
    }

    public String getDateAjoutPromo() {
        return dateAjoutPromo;
    }

    public void setDateAjoutPromo(String dateAjoutPromo) {
        this.dateAjoutPromo = dateAjoutPromo;
    }

    public String getTyppromo() {
        return typpromo;
    }

    public void setTyppromo(String typpromo) {
        this.typpromo = typpromo;
    }

    public String getImageart() {
        return imageart;
    }

    public void setImageart(String imageart) {
        this.imageart = imageart;
    }

    public String getIdpromo() {
        return idpromo;
    }

    public void setIdpromo(String idpromo) {
        this.idpromo = idpromo;
    }

    public int getId_promo() {
        return id_promo;
    }

    public void setId_promo(int id_promo) {
        this.id_promo = id_promo;
    }

    //Méthodes de l'interface Parcelable
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id_promo);
        dest.writeString(this.idpromo);
        dest.writeString(this.txtPromo);
        dest.writeString(this.imageart);
        dest.writeString(this.typpromo);
        dest.writeString(this.idmagasin);
        dest.writeString(this.dateAjoutPromo);
    }

    protected PromoBeaconMetier(Parcel in) {
        this.id_promo = in.readInt();
        this.idpromo = in.readString();
        this.txtPromo = in.readString();
        this.imageart = in.readString();
        this.typpromo = in.readString();
        this.idmagasin = in.readString();
        this.dateAjoutPromo = in.readString();
    }

    public static final Parcelable.Creator<PromoBeaconMetier> CREATOR = new Parcelable.Creator<PromoBeaconMetier>() {
        @Override
        public PromoBeaconMetier createFromParcel(Parcel in) {
            return new PromoBeaconMetier(in);
        }

        @Override
        public PromoBeaconMetier[] newArray(int size) {
            return new PromoBeaconMetier[size];
        }
    };

}
