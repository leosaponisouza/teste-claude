package com.exoma.service.participante;

import com.exoma.dao.ParticipanteDAO;
import com.exoma.pojo.participante;

public class ParticipanteService {
    private ParticipanteDAO participanteDAO = new ParticipanteDAO();

    public participante findParticipanteByCpf(String cpf) {

        return participanteDAO.findParticipanteByCpf(cpf);
    }
}
