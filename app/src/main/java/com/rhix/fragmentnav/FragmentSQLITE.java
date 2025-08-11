package com.rhix.fragmentnav;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FragmentSQLITE extends Fragment {

    private DatabaseHandler db;
    private ListView listView;
    private ProductAdapter adapter;
    private byte[] selectedImageBytes;
    private ImageView previewImageView; // Image preview in dialog

    // Modern ActivityResultLauncher for image picking
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public FragmentSQLITE() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sqlite, container, false);

        db = new DatabaseHandler(getContext());
        listView = root.findViewById(R.id.listViewProducts);
        FloatingActionButton fabAdd = root.findViewById(R.id.fabAddProduct);

        loadProducts();

        // Register image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null && previewImageView != null) {
                            previewImageView.setImageURI(imageUri);
                        }
                        try {
                            InputStream iStream = getActivity().getContentResolver().openInputStream(imageUri);
                            selectedImageBytes = getBytes(iStream);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        fabAdd.setOnClickListener(v -> showAddProductDialog());

        return root;
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText etName = dialogView.findViewById(R.id.etProductName);
        EditText etDescription = dialogView.findViewById(R.id.etProductDescription);
        EditText etPrice = dialogView.findViewById(R.id.etProductPrice);
        previewImageView = dialogView.findViewById(R.id.ivPreview);
        MaterialButton btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);

        btnSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Default dialog save/cancel buttons
        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || selectedImageBytes == null) {
                Toast.makeText(getContext(), "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid price format", Toast.LENGTH_SHORT).show();
                return;
            }

            Product product = new Product(name, description, price, selectedImageBytes);
            db.addProduct(product);
            loadProducts(); // Refresh ListView
            Toast.makeText(getContext(), "Product added", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void loadProducts() {
        List<Product> products = db.getAllProducts();
        adapter = new ProductAdapter(getContext(), products);
        listView.setAdapter(adapter);
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}