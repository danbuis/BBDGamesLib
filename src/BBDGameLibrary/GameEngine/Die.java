package BBDGameLibrary.GameEngine;

import java.util.Arrays;

public class Die<T> {

    //How many sides does this die have
    public final int sides;

    //if it has non-numeric faces what are they?
    public final T[] faces;

    private T currentFace;

    public Die(int sides){
        this.sides = sides;

        Integer[] faces = new Integer[sides];
        for (int i = 1; i <= sides; i++){
            faces[i-1] = i;
        }
        this.faces = (T[]) (T[]) faces;
    }

    public Die(T[] faces){
        this.sides = faces.length;
        this.faces = faces;
    }

    public void roll(){
        this.currentFace = this.faces[(int)Math.floor(Math.random() * this.sides)];
    }

    public boolean setToFace(T facing){
        int indexOfFace = Arrays.asList(this.faces).indexOf(facing);
        if(indexOfFace != -1){
            this.currentFace = facing;
        }
        return indexOfFace != -1;
    }

    public boolean isUnrolled(){
        return currentFace == null;
    }

    public T getCurrentFace(){
        return this.currentFace;
    }
}
