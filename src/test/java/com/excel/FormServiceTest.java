package com.excel;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FormServiceTest
{

    @Test
    void ifPartExists() throws SQLException
    {
        FormService fs = new FormService();
        UUID id = UUID.randomUUID();
        Component comp = new Component(id, "advanced engine part", 112.11, "engine category");
    }
}