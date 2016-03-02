package twilightforest.entity.boss;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.entity.boss.EntityTFLichBolt;

public class EntityTFLichBomb extends EntityThrowable {

   public EntityTFLichBomb(World par1World, double par2, double par4, double par6) {
      super(par1World, par2, par4, par6);
   }

   public EntityTFLichBomb(World par1World, EntityLivingBase par2EntityLiving) {
      super(par1World, par2EntityLiving);
   }

   public EntityTFLichBomb(World par1World) {
      super(par1World);
   }

   protected float func_40077_c() {
      return 0.35F;
   }

   public void onUpdate() {
      super.onUpdate();
      this.makeTrail();
   }

   public void makeTrail() {
      for(int i = 0; i < 1; ++i) {
         double sx = 0.5D * (super.rand.nextDouble() - super.rand.nextDouble()) + super.motionX;
         double sy = 0.5D * (super.rand.nextDouble() - super.rand.nextDouble()) + super.motionY;
         double sz = 0.5D * (super.rand.nextDouble() - super.rand.nextDouble()) + super.motionZ;
         double dx = super.posX + sx;
         double dy = super.posY + sy;
         double dz = super.posZ + sz;
         super.worldObj.spawnParticle("flame", dx, dy, dz, sx * -0.25D, sy * -0.25D, sz * -0.25D);
      }

   }

   public boolean isBurning() {
      return true;
   }

   public boolean canBeCollidedWith() {
      return true;
   }

   public float getCollisionBorderSize() {
      return 1.0F;
   }

   public boolean attackEntityFrom(DamageSource damagesource, float i) {
      this.setBeenAttacked();
      if(damagesource.getEntity() != null) {
         this.explode();
         return true;
      } else {
         return false;
      }
   }

   protected void explode() {
      float explosionPower = 2.0F;
      super.worldObj.newExplosion(this, super.posX, super.posY, super.posZ, explosionPower, false, false);
      this.setDead();
   }

   protected float getGravityVelocity() {
      return 0.001F;
   }

   protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
      boolean passThrough = false;
      if(par1MovingObjectPosition.entityHit != null && (par1MovingObjectPosition.entityHit instanceof EntityTFLichBolt || par1MovingObjectPosition.entityHit instanceof EntityTFLichBomb)) {
         passThrough = true;
      }

      if(par1MovingObjectPosition.entityHit != null && par1MovingObjectPosition.entityHit instanceof EntityTFLich) {
         passThrough = true;
      }

      if(!passThrough) {
         this.explode();
      }

   }
}
