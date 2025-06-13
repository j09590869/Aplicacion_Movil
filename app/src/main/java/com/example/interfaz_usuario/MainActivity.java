package com.example.interfaz_usuario;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextInputEditText etCedula, etNombres, etFechaNacimiento, etCiudad, etCorreo, etTelefono;
    RadioGroup rgGenero;
    Button btnLimpiar, btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a los campos y botones
        etCedula = findViewById(R.id.etCedula);
        etNombres = findViewById(R.id.etNombres);
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        etCiudad = findViewById(R.id.etCiudad);
        etCorreo = findViewById(R.id.etCorreo);
        etTelefono = findViewById(R.id.etTelefono);
        rgGenero = findViewById(R.id.rgGenero);

        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnEnviar = findViewById(R.id.btnEnviar);

        btnLimpiar.setOnClickListener(v -> limpiarFormulario());

        etFechaNacimiento.setOnClickListener(v -> mostrarDatePicker());

        btnEnviar.setOnClickListener(v -> {
            if (validarCampos()) {
                enviarDatos();
            }
        });
    }

    private void mostrarDatePicker() {
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String fecha = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
            etFechaNacimiento.setText(fecha);
        }, anio, mes, dia);

        dpd.show();
    }

    private void limpiarFormulario() {
        etCedula.setText("");
        etNombres.setText("");
        etFechaNacimiento.setText("");
        etCiudad.setText("");
        rgGenero.clearCheck();
        etCorreo.setText("");
        etTelefono.setText("");
    }

    private boolean validarCampos() {
        String cedula = etCedula.getText().toString().trim();
        String nombres = etNombres.getText().toString().trim();
        String fecha = etFechaNacimiento.getText().toString().trim();
        String ciudad = etCiudad.getText().toString().trim();
        int generoId = rgGenero.getCheckedRadioButtonId();
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        if (cedula.isEmpty()) {
            etCedula.setError("Ingrese cédula");
            return false;
        }
        if (!cedula.matches("\\d{1,10}")) {
            etCedula.setError("Cédula solo números, máximo 10 dígitos");
            return false;
        }

        if (nombres.isEmpty()) {
            etNombres.setError("Ingrese nombres");
            return false;
        }
        if (nombres.length() > 500) {
            etNombres.setError("Máximo 500 caracteres");
            return false;
        }
        etNombres.setText(nombres.toUpperCase());

        if (fecha.isEmpty()) {
            etFechaNacimiento.setError("Ingrese fecha de nacimiento");
            return false;
        }

        if (ciudad.isEmpty()) {
            etCiudad.setError("Ingrese ciudad");
            return false;
        }
        if (ciudad.length() > 200) {
            etCiudad.setError("Máximo 200 caracteres");
            return false;
        }
        etCiudad.setText(ciudad.toUpperCase());

        if (generoId == -1) {
            Toast.makeText(this, "Seleccione un género", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (correo.isEmpty()) {
            etCorreo.setError("Ingrese correo electrónico");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            etCorreo.setError("Correo inválido");
            return false;
        }

        if (telefono.isEmpty()) {
            etTelefono.setError("Ingrese teléfono");
            return false;
        }
        if (!telefono.matches("\\d+")) {
            etTelefono.setError("Teléfono solo números");
            return false;
        }

        return true;
    }

    private void enviarDatos() {
        String cedula = etCedula.getText().toString().trim();
        String nombres = etNombres.getText().toString().trim().toUpperCase();
        String fecha = etFechaNacimiento.getText().toString().trim();
        String ciudad = etCiudad.getText().toString().trim().toUpperCase();

        int generoId = rgGenero.getCheckedRadioButtonId();
        RadioButton rbGenero = findViewById(generoId);
        String genero = rbGenero.getText().toString();

        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();

        Intent intent = new Intent(this, ResultadoActivity.class);
        intent.putExtra("cedula", cedula);
        intent.putExtra("nombres", nombres);
        intent.putExtra("fecha", fecha);
        intent.putExtra("ciudad", ciudad);
        intent.putExtra("genero", genero);
        intent.putExtra("correo", correo);
        intent.putExtra("telefono", telefono);

        startActivity(intent);
    }
}
