package BBDGameLibrary.GameEngine;

import java.util.Arrays;

public class Die {

    //How many sides does this die have
    public final int sides;

    //if it has non-numeric faces what are they?
    public final Object[] faces;

    private Object currentFace;

    public Die(int sides){
        this.sides = sides;

        Integer[] faces = new Integer[sides];
        for (int i = 1; i <= sides; i++){
            faces[i-1] = i;
        }
        this.faces = faces;
    }

    public Die(Object[] faces){
        this.sides = faces.length;
        this.faces = faces;
    }

    public void roll(){
        this.currentFace = this.faces[(int)Math.floor(Math.random() * this.sides)];
    }

    public void setToFace(Object facing){
        int indexOfFace = Arrays.asList(this.faces).indexOf(facing);
        if(indexOfFace != -1){
            this.currentFace = facing;
        }
    }

    public boolean isUnrolled(){
        return currentFace == null;
    }

    public Object getCurrentFace(){
        return this.currentFace;
    }
}
