package com.example.chris.nearbykos2;

import model.ColBank;
import model.ColFasilitas;
import model.ColHomeDetail;
import model.ColKota;

/**
 * Created by christian on 10/11/17.
 */

public class JSONResponse {
    private ColKota[] kota;
    public ColKota[] getKota() {
        return kota;
    }

    private ColBank[] bank;
    public ColBank[] getbank() {
        return bank;
    }

    private ColFasilitas[] fasilitas;
    public ColFasilitas[] getFasilitas() {
        return fasilitas;
    }

    private ColHomeDetail[] kriteriaKos;
    public ColHomeDetail[] getKriteriaKos() {
        return kriteriaKos;
    }
}
