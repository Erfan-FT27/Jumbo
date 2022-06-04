package com.jumbo.customerpanel.model;

/**
 * Use capability of JsonView which helps to produce differenct
 * dosage of data based on requirements(API)
 */
public class View {

    public interface Simple {
    }

    public interface Detailed extends Simple {
    }
}
