package org.alejandrocarrillo.dto;

import org.alejandrocarrillo.model.TicketSoporte;

public class TicketSoporteDTO {
    private static TicketSoporteDTO instance;
    private TicketSoporte ticket;
    
    private TicketSoporteDTO(){
        
    }
    
    public static TicketSoporteDTO getTicketSoporteDTO(){
        if(instance == null){
            instance = new TicketSoporteDTO();
        }
        return instance;
    }

    public TicketSoporte getTicketSoporte() {
        return ticket;
    }

    public void setTicketSoporte(TicketSoporte ticket) {
        this.ticket = ticket;
    }    
}
