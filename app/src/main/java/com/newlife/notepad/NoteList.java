package com.newlife.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Notification;
import android.app.NotificationManager;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.content.Context;
import android.content.Intent;
import android.widget.GridView;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NoteList extends AppCompatActivity implements OnItemClickListener{

    private static final int DELETE_ID = Menu.FIRST;

    private NotesDbAdapter mDbHelper;
    private NotificationManager notifiManager;

    private GridView gridview;

    private boolean sortOrder = false; // DESC

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_grid);

        gridview = (GridView)findViewById(R.id.grid);

        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();

        gridview.setOnItemClickListener(this);
        registerForContextMenu(gridview);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if(sortOrder){
            fillData("ASC");
        }else{
            fillData("DESC");
        }
    }

    /* Show list note added*/
    @SuppressWarnings("deprecation")
    private void fillData(String sortOrder) {
        // TODO Auto-generated method stub
        Cursor notesCursor = mDbHelper.readAllNotes(sortOrder);
        startManagingCursor(notesCursor);

        String[] from = new String[]{NotesDbAdapter.KEY_TITLE, NotesDbAdapter.KEY_DATE, NotesDbAdapter.KEY_BODY};
        int[] to = new int[]{R.id.tv_note_grid_title, R.id.tv_note_grid_date, R.id.tv_note_grid_body};

        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.note_column, notesCursor, from, to);
        gridview.setAdapter(notes);
        gridview.setEmptyView(findViewById(R.id.empty));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_item_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.add_menu:
                createNote();
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        // TODO Auto-generated method stub
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case DELETE_ID:
                mDbHelper.deleteNote(info.id);
                if(sortOrder){
                    fillData("ASC");
                }else{
                    fillData("DESC");
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Intent intent_edit = new Intent(getApplicationContext(), NoteEdit.class);
        intent_edit.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivity(intent_edit);
    }

    private void createNote() {
        // TODO Auto-generated method stub
        Intent intent_create = new Intent(this, NoteEdit.class);
        startActivity(intent_create);
    }
}
