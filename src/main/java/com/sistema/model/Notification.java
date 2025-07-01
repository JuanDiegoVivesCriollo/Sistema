package com.sistema.model;

import java.util.Date;

public class Notification {
    private int id;
    private int pedidoId;
    private String mensaje;
    private Date fechaNotificacion;

    public Notification() {}

    public Notification(int id, int pedidoId, String mensaje, Date fechaNotificacion) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.mensaje = mensaje;
        this.fechaNotificacion = fechaNotificacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPedidoId() { return pedidoId; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Date getFechaNotificacion() { return fechaNotificacion; }
    public void setFechaNotificacion(Date fechaNotificacion) { this.fechaNotificacion = fechaNotificacion; }
}