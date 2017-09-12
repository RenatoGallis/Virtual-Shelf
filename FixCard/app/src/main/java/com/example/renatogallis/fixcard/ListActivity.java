package com.example.renatogallis.fixcard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.renatogallis.fixcard.Adapter.LivroAdapter;
import com.example.renatogallis.fixcard.Model.Livro;
import com.example.renatogallis.fixcard.dao.Scripts_Table_Livros;
import com.example.renatogallis.fixcard.Utills.Utilitarios;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView rvLivros;
    private LivroAdapter livroAdapter;
    List<Livro> lista = new LinkedList<>();
    Scripts_Table_Livros scripts_table_livros = new Scripts_Table_Livros(this);
    SQLiteDatabase conn;
    byte[] imagem;
    Utilitarios util = new Utilitarios();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        rvLivros = (RecyclerView) findViewById(R.id.rvLivros);
        livroAdapter = new LivroAdapter(new ArrayList<Livro>());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvLivros.setLayoutManager(layoutManager);
        rvLivros.setAdapter(livroAdapter);
        rvLivros.setHasFixedSize(true);
       // Log.d("LIST COUNT DA TABLE", String.valueOf(DatabaseUtils.queryNumEntries(scripts_table_livros.returnConection(), scripts_table_livros.TABLE_LIVRO)));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvLivros.addItemDecoration(itemDecoration);


        rvLivros.addOnItemTouchListener(new RecyclerTouchListener(this, rvLivros,
                new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (lista.get(position).getFoto() != null) {
                            imagem = util.BitMaptoByteArray(lista.get(position).getFoto());
                        } else {
                            imagem = null;
                        }
                        Intent intent = new Intent(getApplicationContext(), AlterarActivity.class);
                        intent.putExtra("FOTO", imagem);
                        intent.putExtra("TITULO", lista.get(position).getTitulo());
                        intent.putExtra("AUTOR", lista.get(position).getAutor());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(final View view, final int position) {
                        executaDelete(position);

                    }
                }));

        carregaDados();

    }

// controlando o botão voltar do celular
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), PrincipalActivit.class);
        startActivity(intent);
    }
// Irá efetuar o appemd das informações na recyclerview
    public void carregaDados() {
        lista =  scripts_table_livros.retornaTodosLivros();
        livroAdapter.update(lista);
    }

// Criando a interface para utilizar o click na recyclerview... coloquei direto na classe
    // onde tem a recyclerview pois ela é usada somente aqui.


    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }


    public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        // Implementação da interface onClickListiner:
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clicklistener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = rvLivros.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, rvLivros.getChildAdapterPosition(child));
                    }


                }
            });


        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void executaDelete(final int position) {


        AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
        builder.setMessage(getString(R.string.Menssagem_Alert_Deletar) + "\n" + "\n" + lista.get(position).getTitulo())
                .setPositiveButton(R.string.OK_Botao_Alert_Dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        scripts_table_livros.deletarLivro(lista.get(position).getTitulo());
                        livroAdapter.removerLivro(position);

                    }
                })
                .setNegativeButton(R.string.Botao_Cancel_Alert_Dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
        builder.create().show();
    }


    public void adicionar(View view) {
        Intent intent = new Intent(this.getApplicationContext(), AddBookActivity.class);
        startActivity(intent);

    }


}
