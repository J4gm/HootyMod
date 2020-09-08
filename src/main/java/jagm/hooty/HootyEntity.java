package jagm.hooty;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class HootyEntity extends Entity {

	private PlayerEntity closestPlayer;
	private boolean hostile = false;

	// Used by the rendering code to decide whether or not to render the frontmost
	// body segment.
	private boolean hasRetracted = false;

	// List of body segments. Lower indices are closer to the head.
	private List<HootySegment> segments;

	public HootyEntity(EntityType<? extends Entity> entityType, World world) {
		super(entityType, world);
		segments = new ArrayList<HootySegment>();
		this.ignoreFrustumCheck = true;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public void tick() {

		super.tick();
		this.setMotion(Vector3d.ZERO);

		this.closestPlayer = this.world.getClosestPlayer(this, 64.0D);

		if (this.closestPlayer != null) {

			// Normalised vector between Hooty and the player.
			Vector3d extend = new Vector3d(this.closestPlayer.getPosX() - this.getPosX(),
					this.closestPlayer.getPosY() + (double) this.closestPlayer.getEyeHeight() - 0.25D - this.getPosY(),
					this.closestPlayer.getPosZ() - this.getPosZ()).normalize();

			// Convert vector to yaw and pitch so that Hooty faces the player.
			this.rotationYaw = (float) Math.atan2(extend.x, extend.z) * -(180F / (float) Math.PI);
			this.rotationPitch = (float) Math.asin(-extend.y) * (180F / (float) Math.PI);

			// Move Hooty towards the player if the player is further than eight blocks
			// away.
			if (this.getDistance(closestPlayer) > 8.0D) {
				this.setMotion(extend.scale(0.2F));
				this.move(MoverType.SELF, this.getMotion());
				// Add a new body segment if the head is further than 1/4 of a block from the
				// previous segment.
				if (segments.size() <= 0) {
					segments.add(0, new HootySegment(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch));
				} else {
					if (segments.get(0).getDistance(this.getPosX(), this.getPosY(), this.getPosZ()) > 0.25D) {
						segments.add(0, new HootySegment(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, this.rotationPitch));
						hasRetracted = false;
						if (!hostile && segments.size() > 32) {
							hostile = true;
						}
						// Remove the furthest body segment once the maximum has been reached to reduce
						// lag.
						if (segments.size() > 512) {
							segments.remove(segments.size() - 1);
						}
					}
				}
			}
			// Retract Hooty if the player gets closer than six blocks.
			else if (segments.size() > 1) {
				HootySegment firstSegment = segments.get(0);
				if (firstSegment.getDistance(closestPlayer.getPosX(), closestPlayer.getPosY(), closestPlayer.getPosZ()) < 6.0D) {
					Vector3d retract = new Vector3d(firstSegment.getX() - this.getPosX(), firstSegment.getY() - this.getPosY(),
							firstSegment.getZ() - this.getPosZ());
					this.setMotion(retract);
					this.move(MoverType.SELF, this.getMotion());
					segments.remove(0);
					hasRetracted = true;
				}
			}
			// Kill the player if they dare approach Hooty's home.
			else if (this.getDistance(closestPlayer) < 2.0D && hostile) {
				closestPlayer.attackEntityFrom(new DamageSource("hooty").setDamageBypassesArmor(), 9.0F);
			}

		}

	}

	@Override
	protected void registerData() {
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public List<HootySegment> getSegments() {
		return this.segments;
	}

	public boolean getHasRetracted() {
		return hasRetracted;
	}

}
