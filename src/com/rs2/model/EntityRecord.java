package com.rs2.model;

import com.rs2.model.players.Player;

public class EntityRecord {

	private int entityId;
	private boolean entityIsPlayer;
    private Entity entity;
    private String name;

	public EntityRecord(Entity entity) {
        this.entity = entity;
		this.entityId = entity.getUniqueId();
		this.entityIsPlayer = entity.isPlayer();
		if(entity.isPlayer()){
			Player player13 = (Player) entity;
			this.name = player13.getUsername();
		}
	}

	public Entity getEntity() {
        if (entity == null || entity.getIndex() == -1) {
            entity = null;
            Entity[] list = entityIsPlayer ? World.getPlayers() : World.getNpcs();
            for (Entity e: list) {
                if (e == null)
                    continue;
                if(e.isPlayer() && entityIsPlayer){
                	Player player12 = (Player) e;
                	if(player12.getUsername().equals(name)){
                		return e;
                	}
                }
                if(!entityIsPlayer)
                if (e.getUniqueId() == entityId) {
                    entity = e;
                    break;
                }
            }
        }
        return entity;
	}
    
    @Override
    public boolean equals(Object o) {
        if (o == null || (!(o instanceof EntityRecord) && !(o instanceof Entity)))
            return false;
        if (o instanceof Entity) {
            Entity other = (Entity)o;
            if(other.isPlayer() && entityIsPlayer){
            	Player player12 = (Player) other;
            	if(player12.getUsername().equals(name))
            		return true;
            	else
            		return false;
            }
            if (entity == other)
                return true;
            else if (other.isPlayer() == entityIsPlayer && other.getUniqueId() == entityId) {
                entity = other;
                return true;
            } else return false;
        } else {
            EntityRecord other = (EntityRecord)o;
            if(other.entityIsPlayer && entityIsPlayer){
            	if(other.name.equals(name))
            		return true;
            	return false;
            }
            return other.entityId == entityId && other.entityIsPlayer == entityIsPlayer;
        }
    }

}
