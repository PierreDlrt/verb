package dlrt.pierre.conjugaison;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;


import java.util.List;

@Dao
public interface VerbDao {

    @Query("SELECT * FROM verb")
    List<Verb> getAll();

    @Query("SELECT fil FROM verb")
    List<Integer> getAllfil();

    @Query("SELECT verb FROM verb")
    List<String> getAllVerb();

    @Query("SELECT _id, verb FROM verb ORDER BY verb")
    Cursor getAllCursorVerb();

    @Query("SELECT * FROM verb WHERE verb.verb = (:sel)")
    Verb getVerb(String sel);

    @Query("SELECT * FROM verb WHERE verb._id = (:nsel)")
    Verb getVerbFromId(int nsel);

    @Query("SELECT _id FROM verb")
    List<Integer> getAllId();

    @Insert
    void insert(Verb verb);

    @Delete
    void delete(Verb verb);

    @Update
    void update(Verb verb);
}


