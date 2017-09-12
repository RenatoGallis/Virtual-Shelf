package com.example.renatogallis.fixcard.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.renatogallis.fixcard.Model.Livro;
import com.example.renatogallis.fixcard.R;


import java.util.List;

/**
 * Created by Renato Gallis on 07/08/2017.
 */

public class LivroAdapter extends RecyclerView.Adapter<LivroAdapter.LivroViewHolder> {
    private List<Livro> listlivros;

    public LivroAdapter(List<Livro> listlivros) {
        this.listlivros = listlivros;
    }

    @Override
    public LivroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View meuLayout = inflater.inflate(R.layout.livro_row, parent, false);

        return new LivroViewHolder(meuLayout);
    }

    @Override
    public void onBindViewHolder(LivroViewHolder holder, int position) {
        holder.tvAutor.setText(listlivros.get(position).getAutor());
        holder.tvTitulo.setText(listlivros.get(position).getTitulo());
        holder.iv_livro.setImageBitmap(listlivros.get(position).getFoto());
    }



    @Override
    public int getItemCount() {
        return listlivros.size();
    }


    public class LivroViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_livro;
        public TextView tvTitulo;
        public TextView tvAutor;


        public LivroViewHolder(View itemView) {
            super(itemView);

            iv_livro = (ImageView) itemView.findViewById(R.id.iv_livro);
            tvTitulo = (TextView) itemView.findViewById(R.id.tvTitulo);
            tvAutor = (TextView) itemView.findViewById(R.id.tvAutor);
        }
    }

    public void update(List<Livro> ListLivro) {
        this.listlivros = ListLivro;
        notifyDataSetChanged();
    }

    public void removerLivro(int position){
        listlivros.remove(position);
        notifyItemRemoved(position);
    }


}


