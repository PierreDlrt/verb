package dlrt.pierre.conjugaison;

public class VxT {

    private String io;
    private String tu;
    private String lui;
    private String noi;
    private String voi;
    private String loro;

    public VxT(String io, String tu, String lui, String noi, String voi, String loro){

        this.io = io;
        this.tu = tu;
        this.lui = lui;
        this.noi = noi;
        this.voi = voi;
        this.loro = loro;
    }

    public String getIo() { return io; }
    public void setIo(String io) { this.io = io; }

    public String getTu() { return tu; }
    public void setTu(String tu) { this.tu = tu; }

    public String getLui() { return lui; }
    public void setLui(String lui) { this.lui = lui; }

    public String getNoi() { return noi; }
    public void setNoi(String noi) { this.noi = noi; }

    public String getVoi() { return voi; }
    public void setVoi(String voi) { this.voi = voi; }

    public String getLoro() { return loro; }
    public void setLoro(String loro) { this.loro = loro; }

    public String getPers(int selP) {
        switch (selP) {
            case 0: return io;
            case 1: return tu;
            case 2: return lui;
            case 3: return noi;
            case 4: return voi;
            case 5: return loro;
            default: return "";
        }
    }

    public String dispPers(int p){
        switch (p) {
            case 0: return "Io";
            case 1: return "Tu";
            case 2: return "Lui";
            case 3: return "Noi";
            case 4: return "Voi";
            case 5: return "Loro";
            default: return "";
        }
    }

}
