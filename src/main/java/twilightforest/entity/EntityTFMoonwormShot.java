package twilightforest.entity;

import com.gamerforea.eventhelper.util.EventUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;

public class EntityTFMoonwormShot extends EntityThrowable {

   public EntityTFMoonwormShot(World par1World, double par2, double par4, double par6) {
      super(par1World, par2, par4, par6);
   }

   public EntityTFMoonwormShot(World par1World, EntityLivingBase par2EntityLiving) {
      super(par1World, par2EntityLiving);
   }

   public EntityTFMoonwormShot(World par1World) {
      super(par1World);
   }

   protected float func_40077_c() {
      return 0.5F;
   }

   public void onUpdate() {
      super.onUpdate();
      this.makeTrail();
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   public void makeTrail() {}

   public boolean canBeCollidedWith() {
      return true;
   }

   public float getCollisionBorderSize() {
      return 1.0F;
   }

   protected float getGravityVelocity() {
      return 0.03F;
   }

   protected void onImpact(MovingObjectPosition mop) {
      if(mop.typeOfHit == MovingObjectType.BLOCK && !super.worldObj.isRemote) {
	  //LeRioN fix
	   if (!EventUtils.cantBreak((EntityPlayer)super.getThrower(), mop.blockX, mop.blockY, mop.blockZ)) 
         TFItems.moonwormQueen.onItemUse((ItemStack)null, (EntityPlayer)this.getThrower(), super.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, 0.0F, 0.0F, 0.0F);
      //LeRioN fix
	  }

      if(mop.entityHit != null) {
         mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
      }

      for(int var3 = 0; var3 < 8; ++var3) {
         super.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(TFBlocks.moonworm) + "_0", super.posX, super.posY, super.posZ, 0.0D, 0.0D, 0.0D);
      }

      if(!super.worldObj.isRemote) {
         this.setDead();
      }

   }
}
