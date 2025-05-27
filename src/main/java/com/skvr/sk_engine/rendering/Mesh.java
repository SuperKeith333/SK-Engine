package com.skvr.sk_engine.rendering;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;

public class Mesh {

    int VBO, VAO, EBO = -1, texCoordsVBO = -1;
    int numVertices;

    public Mesh(float[] vertices) {
        numVertices = vertices.length / 3;

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        FloatBuffer vertexBuffer = memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        memFree(vertexBuffer);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
    }
    public Mesh(float[] vertices, float[] texCoords) {
        numVertices = vertices.length / 3;

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        texCoordsVBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        FloatBuffer vertexBuffer = memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        memFree(vertexBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, texCoordsVBO);
        FloatBuffer texCoordsBuffer = memAllocFloat(texCoords.length);
        texCoordsBuffer.put(texCoords).flip();
        glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        memFree(texCoordsBuffer);
    }
    public Mesh (float[] vertices, int[] indices) {
        numVertices = indices.length;

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);

        FloatBuffer vertexBuffer = memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);

        IntBuffer indicesBuffer = memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        memFree(vertexBuffer);
        memFree(indicesBuffer);
    }

    public Mesh (float[] vertices, int[] indices, float[] texCoords) {
        numVertices = indices.length;

        VAO = glGenVertexArrays();
        VBO = glGenBuffers();
        EBO = glGenBuffers();
        texCoordsVBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        FloatBuffer vertexBuffer = memAllocFloat(vertices.length);
        vertexBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        memFree(vertexBuffer);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        IntBuffer indicesBuffer = memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
        memFree(indicesBuffer);

        glBindBuffer(GL_ARRAY_BUFFER, texCoordsVBO);
        FloatBuffer texCoordsBuffer = memAllocFloat(texCoords.length);
        texCoordsBuffer.put(texCoords).flip();
        glBufferData(GL_ARRAY_BUFFER, texCoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(1);
        memFree(texCoordsBuffer);
    }

    public void draw() {

        if (EBO != -1) {
            glBindVertexArray(VAO);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);

            glDrawElements(GL_TRIANGLES, numVertices, GL_UNSIGNED_INT, 0);
            return;
        }

        glBindVertexArray(VAO);
        glDrawArrays(GL_TRIANGLES, 0, numVertices);
    }

    public void draw(int start, int numVertices) {
        glBindVertexArray(VAO);

        if (EBO != -1) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
            if (start < this.numVertices * 4) {
                if (numVertices <= this.numVertices) {
                    glDrawElements(GL_TRIANGLES, numVertices, GL_UNSIGNED_INT, start);
                } else {
                    glDrawElements(GL_TRIANGLES, this.numVertices, GL_UNSIGNED_INT, start);
                    System.err.println("The Number of vertices is too high, using default");
                }
            } else {
                if (numVertices <= this.numVertices) {
                    glDrawElements(GL_TRIANGLES, numVertices, GL_UNSIGNED_INT, 0);
                    System.err.println("The start vertex is too high, using default");
                } else {
                    glDrawElements(GL_TRIANGLES, this.numVertices, GL_UNSIGNED_INT, 0);
                    System.err.println("The Number of vertices and start vertex is too high, using default");
                }
            }
            return;
        }

        if (start < this.numVertices) {
            if (numVertices <= this.numVertices) {
                glDrawArrays(GL_TRIANGLES, start, numVertices);
            } else {
                glDrawArrays(GL_TRIANGLES, start, this.numVertices);
                System.err.println("The Number of vertices is too high, using default");
            }
        } else {
            if (numVertices <= this.numVertices) {
                glDrawArrays(GL_TRIANGLES, 0, numVertices);
                System.err.println("The start vertex is too high, using default");
            } else {
                glDrawArrays(GL_TRIANGLES, 0, this.numVertices);
                System.err.println("The Number of vertices and start vertex is too high, using default");
            }
        }
    }
}
