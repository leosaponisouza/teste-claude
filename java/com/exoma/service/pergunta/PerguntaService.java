package com.exoma.service.pergunta;

import com.exoma.dao.PerguntaDcDAO;
import com.exoma.dao.RespostaDcDAO;
import com.exoma.model.PerguntaDc;
import com.exoma.model.RespostaDc;
import java.util.List;
import java.util.Set;

public class PerguntaService {


    private PerguntaDcDAO perguntaDao = new PerguntaDcDAO();
    private RespostaDcDAO respostaDao = new RespostaDcDAO();

    public List<PerguntaDc> findAtivas() {
        return perguntaDao.findAtivas();
    }

    public void saveResposta(Set<RespostaDc> resposta) {
        respostaDao.saveOrUpdate(resposta);
    }



}
