package mx.com.gauta.usedoc.ui.addpaciente;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mx.com.gauta.usedoc.HttpManager;
import mx.com.gauta.usedoc.R;
import mx.com.gauta.usedoc.user_active;

public class AddPacienteFragment extends Fragment {

    private AddPacienteViewModel mViewModel;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA = 1;
    EditText editText;
    Bitmap bitmap;

    public static AddPacienteFragment newInstance() {
        return new AddPacienteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_paciente_fragment, container, false);

        Button face_button = view.findViewById(R.id.add_face_pac);
        editText = view.findViewById(R.id.paciente_code_edit);
        face_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraRe();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddPacienteViewModel.class);
        // TODO: Use the ViewModel
    }

    public void cameraRe(){
        int permission = ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA);

        }

        permission = ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA);

        if (permission == PackageManager.PERMISSION_GRANTED){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == this.getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String img_base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            System.out.println(img_base64);
            uploadImage(img_base64);
        }
    }

    Response.Listener<String> res = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            editText.setText(response);

        }
    };

    private void uploadImage(final String img_base64){
        final RequestQueue queue = Volley.newRequestQueue(this.getContext());
        final String name = user_active.getUser_id()+ new Date().getTime();
        System.out.println(img_base64);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpManager.URL+"image_upload",res, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                editText.setText(error.getMessage());
                error.printStackTrace();

            }
        }){
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("img", img_base64);
                MyData.put("img_name",name);
                return MyData;
            }
        };
        queue.add(stringRequest);

    }

}
