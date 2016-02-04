package com.github.agjacome.httpserver.controller;

import com.github.agjacome.httpserver.view.View;

import static java.util.Objects.requireNonNull;

public abstract class ViewController extends Controller {

    protected final View view;

    protected ViewController(final View view) {
        this.view = requireNonNull(view);
    }

}
