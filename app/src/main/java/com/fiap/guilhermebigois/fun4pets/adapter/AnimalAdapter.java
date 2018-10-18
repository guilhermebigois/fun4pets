package com.fiap.guilhermebigois.fun4pets.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fiap.guilhermebigois.fun4pets.R;
import com.fiap.guilhermebigois.fun4pets.holder.AnimalViewHolder;
import com.fiap.guilhermebigois.fun4pets.model.Animal;

import java.text.SimpleDateFormat;
import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalViewHolder> {
    private List<Animal> animais;

    public AnimalAdapter(List<Animal> animais) {
        this.animais = animais;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.content_lista_animais, parent, false);
        AnimalViewHolder viewHolder = new AnimalViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        if (animais != null && animais.size() > 0) {
            Animal animal = animais.get(position);

            holder.txtNome.setText(animal.getNome());
            holder.txtEspecie.setText(animal.getEspecie());
            holder.txtRaca.setText(animal.getRaca());
            holder.txtColoracao.setText(animal.getColoracao());
            holder.txtSexo.setText((animal.getSexo().equals("M")) ? "Macho" : "Fêmea");

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            holder.txtNascimento.setText(formato.format(animal.getNascimento()));
        }
    }

    @Override
    public int getItemCount() {
        return animais.size();
    }
}
