/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBAccessException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author bourbia.brahim
 * sample code 
 */
@ManagedBean(name = "entrepriseBean")
@SessionScoped
public class AddEntrepriseBean implements Serializable{
    
    
    private String detailimg;
    private String name;
    private UploadedFile file;
//    private Entreprise entreprise;
    private boolean entrepriseExist;
    FacesMessage msg;
    private String imageB64;
    private String witdh;
    private String height;

    /**
     * Creates a new instance of AddEntrepriseBean
     */
   
    public AddEntrepriseBean() {
        detailimg ="";
        imageB64 ="";
        
        witdh = "0px";
        height = "0px";
        
        name = "";
        file = null;
        entrepriseExist = true;
//        entreprise = new Entreprise();
    }
    
    public void testUpload2(FileUploadEvent event){
        System.out.println("te t");
        if(event.getFile() != null ) {
            name = event.getFile().getFileName();
            detailimg = "Fichier = "+name+" | Taille = "+event.getFile().getSize()/1000+" KB";
            System.out.println("file == s >"+event.getFile().getFileName());
            file = event.getFile();
            File f = new File("/temp/sss");
            
            try {
                FileUtils.copyInputStreamToFile(file.getInputstream(), f);
                imageB64  = DatatypeConverter.printBase64Binary(FileUtils.readFileToByteArray(f));
                witdh = "100px";
                height = "170px";
            } catch (IOException ex) {
                Logger.getLogger(AddEntrepriseBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        else System.out.println("No fillllle ");
    }
    
    public void refresh(){
        file = null;
        name = "";
        detailimg ="";
        imageB64 ="";
        witdh = "0px";
        height = "0px";
        
    }
    
    public void addEntreprise(){
        if (entrepriseIdExist()) {
            System.out.println("Entreprise exist Dejà !");
        }else{
            if(file != null ){
                //Recuperation de la requete utilisateur
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                //Recuperer le context d'application à partir de la requête (afin d'acceder au dossier d'application )
                ServletContext servletContext = request.getSession().getServletContext();
                //Recuperer le chemin ou l'application a partir du context precedent pour acceder au dossier d'images en creont un fichier
                String urlicon="images/entreprises-image."+FilenameUtils.getExtension(name);
                // Enregitrer le fihier  dans le dossier images de l'application
                File destination = new File(servletContext.getRealPath("/"+urlicon));
                
                try {
                    FileUtils.copyInputStreamToFile(file.getInputstream(), destination);
                    file.getInputstream().close();
                } catch (IOException ex) {
                    System.out.println("Tabtoob");
                    ex.printStackTrace();
                }
                try {
//                    entreprise.setUrlicone(urlicon);
//                    entrepriseFacade.create(entreprise);
                    msg = new FacesMessage("Entreprisé Ajoutée ", "Succés d'ajout .");
                    msg.setSeverity(FacesMessage.SEVERITY_INFO);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                } catch (EJBAccessException e) {
                    msg = new FacesMessage("Erreur d'insertion dans la base !", "Error fatal EJB");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                }
                
                
                //renitialiser tous le composant du formulaire
//                entreprise = new Entreprise();
                file = null;
                name = "";
                detailimg ="";
                imageB64 ="";
                witdh = "0px";
                height = "0px";  
             }else{
                msg = new FacesMessage("Pas image !","Veuillez charger une image");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(null, msg);   
             }
        }
    }
    
    public boolean entrepriseIdExist(){
		// code not implemented yet
         return false;
    }

    public String getImageB64() {
        return imageB64;
    }

    public void setImageB64(String imageB64) {
        this.imageB64 = imageB64;
    }

    public String getWitdh() {
        return witdh;
    }

    public void setWitdh(String witdh) {
        this.witdh = witdh;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDetailimg() {
        return detailimg;
    }

    public void setDetailimg(String detailimg) {
        this.detailimg = detailimg;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public boolean isEntrepriseExist() {
        return entrepriseExist;
    }

    public void setEntrepriseExist(boolean entrepriseExist) {
        this.entrepriseExist = entrepriseExist;
    }
    
}
