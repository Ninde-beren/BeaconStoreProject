package imie.angers.fr.beaconstoreproject.metiers;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.Date;

/**
 * Permet de créer un objet ConsommateurMetier et d'accéder à ses attributs
 * Implémente l'interface Parcelable permettant de passer des instances d'objets entre activités
 * Created by Anne on 17/02/2016.
 */
public class ConsommateurMetier implements Parcelable{

    //Attributs de l'objet ConsommateurMetier
    private int id_c;
    private int idConso;
    private String email;
    private String password; //md5
    private String adrmac;
    private String tel;
    private String token;
    private String nom;
    private String prenom;
    private String genre;
    private String dtnaiss;
    private String cdpostal;
    private String catsocpf;

    public ConsommateurMetier() {}


    //Getters & Setters
    public String getCatsocpf() {
        return catsocpf;
    }

    public void setCatsocpf(String catsocpf) {
        this.catsocpf = catsocpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdrmac() {
        return adrmac;
    }

    public void setAdrmac(String adrmac) {
        this.adrmac = adrmac;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCdpostal() {
        return cdpostal;
    }

    public void setCdpostal(String cdpostal) {
        this.cdpostal = cdpostal;
    }

    public String getDtnaiss() {
        return dtnaiss;
    }

    public void setDtnaiss(String dtnaiss) {
        this.dtnaiss = dtnaiss;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getId_c() {
        return id_c;
    }

    public void setId_c(int id_c) {
        this.id_c = id_c;
    }

    public int getIdConso() {
        return idConso;
    }

    public void setIdConso(int idConso) {
        this.idConso = idConso;
    }

    //Méthodes de l'interface Parcelable

    protected ConsommateurMetier(Parcel in) {
        id_c = in.readInt();
        idConso = in.readInt();
        email = in.readString();
        password = in.readString();
        adrmac = in.readString();
        tel = in.readString();
        token = in.readString();
        nom = in.readString();
        prenom = in.readString();
        genre = in.readString();
        dtnaiss = in.readString();
        cdpostal = in.readString();
        catsocpf = in.readString();
    }

    public static final Creator<ConsommateurMetier> CREATOR = new Creator<ConsommateurMetier>() {
        @Override
        public ConsommateurMetier createFromParcel(Parcel in) {
            return new ConsommateurMetier(in);
        }

        @Override
        public ConsommateurMetier[] newArray(int size) {
            return new ConsommateurMetier[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_c);
        dest.writeInt(idConso);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(adrmac);
        dest.writeString(tel);
        dest.writeString(token);
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(genre);
        dest.writeString(dtnaiss);
        dest.writeString(cdpostal);
        dest.writeString(catsocpf);
    }
}
