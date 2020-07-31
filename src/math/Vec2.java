package math;

/**
 * Vec2 class.
 * 
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Vec2 {
    
    public double x;
    public double y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void set(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }
    
    public void normalize() {
        double length = getLength();
        if (length > 0) {
            x /= length;
            y /= length;
        }
    }
    
    public void add(Vec2 v) {
        x += v.x;
        y += v.y;
    }

    public void sub(Vec2 v) {
        x -= v.x;
        y -= v.y;
    }

    public void scale(double s) {
        x *= s;
        y *= s;
    }
    
    public Vec2 perp() {
        Vec2 perp = new Vec2(-y, x);
        perp.normalize();
        return perp;
    }
    
    public double perpDot(Vec2 v) {
        return perp().dot(v);
    }
    
    public double dot(Vec2 v) {
        return x * v.x + y * v.y;
    }

    public double cross(Vec2 v) {
        return x * v.y - v.x * y;
    }

    public void rotate(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);
        double nx = x * c - y * s;
        double ny = x * s + y * c;
        set(nx, ny);
    }
    
    @Override
    public String toString() {
        return "Vec2{" + "x=" + x + ", y=" + y + '}';
    }
    
}
