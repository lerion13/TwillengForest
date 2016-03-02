package twilightforest.entity.boss;

import com.gamerforea.eventhelper.util.EventUtils;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFYeti;
import twilightforest.entity.boss.EntityTFYetiAlpha;

public class EntityTFIceBomb extends EntityThrowable {

   private int zoneTimer = 80;
   private boolean hasHit;

   public EntityTFIceBomb(World par1World) {
      super(par1World);
   }

   public EntityTFIceBomb(World par1World, EntityLivingBase thrower) {
	  super(par1World, thrower);
   }

   protected void onImpact(MovingObjectPosition mop) {
      if(this.getThrower() != null && this.getThrower() instanceof EntityTFYetiAlpha) {
         double dist = this.getDistanceSqToEntity(this.getThrower());
         if(dist <= 100.0D) {
            this.setDead();
         }
      }

      super.motionY = 0.0D;
      this.hasHit = true;
      if(!super.worldObj.isRemote) {
         this.doTerrainEffects();
      }

   }

   private void doTerrainEffects() {
      byte range = 3;
      int ix = MathHelper.floor_double(super.lastTickPosX);
      int iy = MathHelper.floor_double(super.lastTickPosY);
      int iz = MathHelper.floor_double(super.lastTickPosZ);
      BreakEvent event = null;
      Block block = null;
      boolean currentMeta = false;

      for(int x = -range; x <= range; ++x) {
         for(int y = -range; y <= range; ++y) {
            for(int z = -range; z <= range; ++z) {
               if(super.getThrower() != null && super.getThrower() instanceof EntityPlayer) {
                  int var11 = super.worldObj.getBlockMetadata(x, y, z);
                  block = super.worldObj.getBlock(x, y, z);
                  event = new BreakEvent(ix + x, iy + y, iz + z, super.worldObj, block, var11, (EntityPlayer)super.getThrower());
                  MinecraftForge.EVENT_BUS.post(event);
                  if(event.isCanceled()) {
                     return;
                  }

                  this.doTerrainEffect(ix + x, iy + y, iz + z);
               } else {
                  this.doTerrainEffect(ix + x, iy + y, iz + z);
               }
            }
         }
      }

   }

   private void doTerrainEffect(int x, int y, int z) {
               	  //LeRioN fix
	  if (!EventUtils.cantBreak((EntityPlayer)super.getThrower(), x, y, z)) 
   
	{//lerion
      if(super.worldObj.getBlock(x, y, z).getMaterial() == Material.water) {
         super.worldObj.setBlock(x, y, z, Blocks.ice);
      }

      if(super.worldObj.getBlock(x, y, z).getMaterial() == Material.lava) {
         super.worldObj.setBlock(x, y, z, Blocks.obsidian);
      }

      if(super.worldObj.isAirBlock(x, y, z) && Blocks.snow_layer.canPlaceBlockAt(super.worldObj, x, y, z)) {
         super.worldObj.setBlock(x, y, z, Blocks.snow_layer);
      }
	} //lerion
   }

   public void onUpdate() {
      super.onUpdate();
      if(this.hasHit) {
         if(!super.worldObj.isRemote) {
            super.motionX *= 0.1D;
            super.motionY *= 0.1D;
            super.motionZ *= 0.1D;
         }

         --this.zoneTimer;
         this.makeIceZone();
         if(this.zoneTimer <= 0) {
            this.detonate();
         }
      } else {
         this.makeTrail();
      }

   }

   public void makeTrail() {
      for(int i = 0; i < 10; ++i) {
         double dx = super.posX + (double)(0.75F * (super.rand.nextFloat() - 0.5F));
         double dy = super.posY + (double)(0.75F * (super.rand.nextFloat() - 0.5F));
         double dz = super.posZ + (double)(0.75F * (super.rand.nextFloat() - 0.5F));
         TwilightForestMod.proxy.spawnParticle(super.worldObj, "snowstuff", dx, dy, dz, 0.0D, 0.0D, 0.0D);
      }

   }

   private void makeIceZone() {
      if(super.worldObj.isRemote) {
         for(int i = 0; i < 20; ++i) {
            double dx = super.posX + (double)((super.rand.nextFloat() - super.rand.nextFloat()) * 3.0F);
            double dy = super.posY + (double)((super.rand.nextFloat() - super.rand.nextFloat()) * 3.0F);
            double dz = super.posZ + (double)((super.rand.nextFloat() - super.rand.nextFloat()) * 3.0F);
            TwilightForestMod.proxy.spawnParticle(super.worldObj, "snowstuff", dx, dy, dz, 0.0D, 0.0D, 0.0D);
         }
      } else if(this.zoneTimer % 10 == 0) {
         this.hitNearbyEntities();
      }

   }

   private void hitNearbyEntities() {
      ArrayList nearby = new ArrayList(super.worldObj.getEntitiesWithinAABBExcludingEntity(this, super.boundingBox.expand(3.0D, 2.0D, 3.0D)));
      Iterator var2 = nearby.iterator();

      while(var2.hasNext()) {
         Entity entity = (Entity)var2.next();
         if(entity instanceof EntityLivingBase && entity != this.getThrower()) {
            if(entity instanceof EntityTFYeti) {
				entity.setDead();
				
				
               int chillLevel1 = MathHelper.floor_double(entity.lastTickPosX);
               int iy = MathHelper.floor_double(entity.lastTickPosY);
               int iz = MathHelper.floor_double(entity.lastTickPosZ);
				super.worldObj.setBlock(chillLevel1, iy, iz, Blocks.ice);
				super.worldObj.setBlock(chillLevel1, iy + 1, iz, Blocks.ice);
            } else 			    //LeRioN fix
				if (!EventUtils.cantDamage((EntityPlayer)super.getThrower(), entity)) {

				
               entity.attackEntityFrom(DamageSource.magic, 1.0F);
               byte chillLevel11 = 2;
               //((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, chillLevel11, true));
            }
         }
      }

   }

   private void detonate() {
      this.setDead();
   }

   public Block getBlock() {
      return Blocks.packed_ice;
   }

   protected float func_70182_d() {
      return 0.75F;
   }

   protected float getGravityVelocity() {
      return this.hasHit?0.0F:0.025F;
   }

   protected float func_70183_g() {
      return -20.0F;
   }
}
