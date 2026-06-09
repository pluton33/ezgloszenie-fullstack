package io.github.pluton33.ezgloszenie.data;

public enum UserRole {
    USER(0),
    MODERATOR(1),
    ADMIN(2);
    private int permissionLevel;
    UserRole(int permissionLevel){
        this.permissionLevel = permissionLevel;
    }
    public int getPermissionLevel()
    {
        return permissionLevel;
    }
}