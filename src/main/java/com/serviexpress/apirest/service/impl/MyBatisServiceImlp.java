package com.serviexpress.apirest.service.impl;

import java.util.List;

import com.serviexpress.apirest.mapper.IngresoVSEgresoMapper;
import com.serviexpress.apirest.mapper.ReservaMapper;
import com.serviexpress.apirest.payload.Response.IngresoVsEgreso;
import com.serviexpress.apirest.payload.Response.ResponseReservaPago;
import com.serviexpress.apirest.service.MyBatisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class MyBatisServiceImlp implements MyBatisService {

    @Autowired
    IngresoVSEgresoMapper ingresoVSEgresoMapper;
    ReservaMapper reservaMapper;
    
    @Override
    public List<IngresoVsEgreso> getEgreso() {

        return ingresoVSEgresoMapper.getEgreso();
    }

    @Override
    public List<IngresoVsEgreso> getIngreso() {

        return ingresoVSEgresoMapper.getIngreso();
    }

    @Override
    public List<ResponseReservaPago> getReservaCliente(String patente) {
        System.out.println(patente);
        return reservaMapper.getReservaCliente(patente);
    }


}
