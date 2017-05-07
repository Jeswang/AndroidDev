package com.xinyu.smartnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xinyu.smartnote.models.Note;
import com.xinyu.smartnote.util.DateUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NoteDao {
    private com.xinyu.smartnote.db.MyOpenHelper helper;
    private DatabaseReference ref;
    private String mUserID;
    private HashMap<String, Note> mNotes;
    private UpdateNoteListListener mListner;

    public NoteDao(Context context, String userID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mUserID = userID;
        mNotes = new HashMap<String, Note>();

        ref = database.getReference("notes/" + mUserID);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Note newNote = dataSnapshot.getValue(Note.class);
                System.out.println("Author: " + newNote.getTitle());
                newNote.setKey(dataSnapshot.getKey());
                mNotes.put(dataSnapshot.getKey(), newNote);
                if (mListner != null) {
                    mListner.updateNoteList(NoteDao.this.queryNotesAll(0));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Note newNote = dataSnapshot.getValue(Note.class);
                System.out.println("Author: " + newNote.getTitle());

                mNotes.put(dataSnapshot.getKey(), newNote);
                if (mListner != null) {
                    mListner.updateNoteList(NoteDao.this.queryNotesAll(0));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mNotes.remove(dataSnapshot.getKey());
                if (mListner != null) {
                    mListner.updateNoteList(NoteDao.this.queryNotesAll(0));
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        helper = new com.xinyu.smartnote.db.MyOpenHelper(context);
    }

    public interface UpdateNoteListListener {
        public void updateNoteList(ArrayList<Note> notes);
    }


    public void setListner(UpdateNoteListListener listner) {
        mListner = listner;
    }

    /**
     * 查询所有笔记
     */
    public ArrayList<Note> queryNotesAll(int groupId) {
        ArrayList<Note> notes = new ArrayList<Note>();

        for (Note note : mNotes.values()) {
            notes.add(note);
        }

        return notes;
    }

    /**
     * 插入笔记
     */
    public String insertNote(Note note) {
        DatabaseReference newNoteRef = ref.push();
        newNoteRef.setValue(note);
        note.setKey(newNoteRef.getKey());

        return newNoteRef.getKey();
    }

    /**
     * 更新笔记
     * @param note
     */
    public void updateNote(Note note) {
        mNotes.put(note.getKey(), note);
        DatabaseReference updateNoteRef = ref.child(note.getKey());
        updateNoteRef.setValue(note);
    }

    /**
     * 删除笔记
     */
    public int deleteNote(Note note) {
        String key = note.getKey();
        mNotes.remove(key);
        DatabaseReference removeNoteRef = ref.child(key);
        removeNoteRef.removeValue();
        return 1;
    }
}
