package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityTFNatureBolt extends EntityThrowable {

   private EntityPlayer playerTarget;


   public EntityTFNatureBolt(World par1World, double par2, double par4, double par6) {
      super(par1World, par2, par4, par6);
   }

   public EntityTFNatureBolt(World par1World, EntityLivingBase par2EntityLiving) {
      super(par1World, par2EntityLiving);
   }

   public EntityTFNatureBolt(World par1World) {
      super(par1World);
   }

   public void onUpdate() {
      super.onUpdate();
      this.makeTrail();
   }

   protected float getGravityVelocity() {
      return 0.003F;
   }

   public void makeTrail() {
      for(int i = 0; i < 5; ++i) {
         double dx = super.posX + 0.5D * (super.rand.nextDouble() - super.rand.nextDouble());
         double dy = super.posY + 0.5D * (super.rand.nextDouble() - super.rand.nextDouble());
         double dz = super.posZ + 0.5D * (super.rand.nextDouble() - super.rand.nextDouble());
         super.worldObj.spawnParticle("happyVillager", dx, dy, dz, 0.0D, 0.0D, 0.0D);
      }

   }

   protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
      if(par1MovingObjectPosition.entityHit != null && par1MovingObjectPosition.entityHit instanceof EntityLivingBase && par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getThrower()), 2.0F)) {
         byte dx = (byte)(super.worldObj.difficultySetting == EnumDifficulty.PEACEFUL?0:(super.worldObj.difficultySetting == EnumDifficulty.NORMAL?3:7));
         if(dx > 0) {
            ((EntityLivingBase)par1MovingObjectPosition.entityHit).addPotionEffect(new PotionEffect(Potion.poison.id, dx * 20, 0));
         }
      }

      int var6;
      for(var6 = 0; var6 < 8; ++var6) {
         super.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(Blocks.leaves) + "_0", super.posX, super.posY, super.posZ, super.rand.nextGaussian() * 0.05D, super.rand.nextDouble() * 0.2D, super.rand.nextGaussian() * 0.05D);
      }

      if(!super.worldObj.isRemote) {
         this.setDead();
         if(par1MovingObjectPosition != null) {
            var6 = MathHelper.floor_double((double)par1MovingObjectPosition.blockX);
            int dy = MathHelper.floor_double((double)par1MovingObjectPosition.blockY);
            int dz = MathHelper.floor_double((double)par1MovingObjectPosition.blockZ);
            Material materialHit = super.worldObj.getBlock(var6, dy, dz).getMaterial();
            if(materialHit == Material.grass && this.playerTarget != null) {
               Items.dye.onItemUse(new ItemStack(Items.dye, 1, 15), this.playerTarget, super.worldObj, var6, dy, dz, 0, 0.0F, 0.0F, 0.0F);
            } else if(materialHit.isSolid() && this.canReplaceBlock(super.worldObj, var6, dy, dz)) {
               super.worldObj.setBlock(var6, dy, dz, Blocks.leaves, 2, 3);
            }
         }
      }

   }

   private boolean canReplaceBlock(World worldObj, int dx, int dy, int dz) {
      Block blockID = worldObj.getBlock(dx, dy, dz);
      float hardness = blockID == null?-1.0F:blockID.getBlockHardness(worldObj, dx, dy, dz);
      return hardness >= 0.0F && hardness < 50.0F;
   }

   public void setTarget(EntityLivingBase attackTarget) {
      if(attackTarget instanceof EntityPlayer) {
         this.playerTarget = (EntityPlayer)attackTarget;
      }

   }
}
