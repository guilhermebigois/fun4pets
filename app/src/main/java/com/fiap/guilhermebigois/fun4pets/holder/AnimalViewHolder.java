package com.fiap.guilhermebigois.fun4pets.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fiap.guilhermebigois.fun4pets.R;

public class AnimalViewHolder extends RecyclerView.ViewHolder {
    public TextView txtNome;
    public TextView txtSexo;
    public TextView txtNascimento;
    public TextView txtEspecie;
    public TextView txtRaca;
    public TextView txtColoracao;

    public AnimalViewHolder(View itemView) {
        super(itemView);

        txtNome = itemView.findViewById(R.id.txtNome);
        txtSexo = itemView.findViewById(R.id.txtSexo);
        txtNascimento = itemView.findViewById(R.id.txtNascimento);
        txtEspecie = itemView.findViewById(R.id.txtEspecie);
        txtRaca = itemView.findViewById(R.id.txtRaca);
        txtColoracao = itemView.findViewById(R.id.txtColoracao);
    }
}
