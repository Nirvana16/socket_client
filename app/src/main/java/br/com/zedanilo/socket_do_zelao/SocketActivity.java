package br.com.zedanilo.socket_do_zelao;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class SocketActivity extends ActionBarActivity {

    String imgDecodableString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_socket);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("onCreateOptionsMenu", "create menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.socket_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menu superior, para abrir galeria e camera
        switch (item.getItemId()) {
            case R.id.action_attach:
                Log.d("onOptionsItemSelected","action_attach");
                openGallery();
                return true;
            case R.id.action_capture:
                Log.d("onOptionsItemSelected","action_capture");
                fotografar();
               // openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openGallery()
    {
        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryintent, 1);
    }

    private void fotografar(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK
                && null != data) {
            //Enviar imagem no chat
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            // move pra primeira linha, é uma gambiarra e usei, mas ta funcionando
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();


            ChatFragment fragment = (ChatFragment) getFragmentManager().findFragmentById(R.id.chat);
            fragment.sendImage(imgDecodableString);
        }
    }

}
