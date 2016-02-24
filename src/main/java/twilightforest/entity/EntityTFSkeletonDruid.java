package twilightforest.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import twilightforest.TFAchievementPage;
import twilightforest.entity.EntityTFNatureBolt;
import twilightforest.item.TFItems;

public class EntityTFSkeletonDruid extends EntityMob implements IRangedAttackMob {

   public EntityTFSkeletonDruid(World world) {
      super(world);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIRestrictSun(this));
      super.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
      super.tasks.addTask(4, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
      super.tasks.addTask(5, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(6, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      this.setCurrentItemOrArmor(0, new ItemStack(Items.golden_hoe));
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
   }

   protected String getLivingSound() {
      return "mob.skeleton.say";
   }

   protected String getHurtSound() {
      return "mob.skeleton.hurt";
   }

   protected String getDeathSound() {
      return "mob.skeleton.death";
   }

   protected void func_145780_a(int par1, int par2, int par3, Block par4) {
      this.playSound("mob.skeleton.step", 0.15F, 1.0F);
   }

   protected Item getDropItem() {
      return TFItems.torchberries;
   }

   protected void dropFewItems(boolean par1, int lootingModifier) {
      int numberOfItemsToDrop = super.rand.nextInt(3 + lootingModifier);

      int i;
      for(i = 0; i < numberOfItemsToDrop; ++i) {
         this.dropItem(TFItems.torchberries, 1);
      }

      numberOfItemsToDrop = super.rand.nextInt(3 + lootingModifier);

      for(i = 0; i < numberOfItemsToDrop; ++i) {
         this.dropItem(Items.bone, 1);
      }

   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
      if(par1DamageSource.getSourceOfDamage() instanceof EntityPlayer) {
         ((EntityPlayer)par1DamageSource.getSourceOfDamage()).triggerAchievement(TFAchievementPage.twilightHunter);
      }

   }

   public void attackEntityWithRangedAttack(EntityLivingBase attackTarget, float extraDamage) {

   }

   protected boolean isValidLightLevel() {
      boolean valid = false;
      int dx = MathHelper.floor_double(super.posX);
      int dy = MathHelper.floor_double(super.boundingBox.minY);
      int dz = MathHelper.floor_double(super.posZ);
      if(super.worldObj.getSavedLightValue(EnumSkyBlock.Sky, dx, dy, dz) > super.rand.nextInt(32)) {
         valid = false;
      } else {
         int light = super.worldObj.getBlockLightValue(dx, dy, dz);
         valid = light <= super.rand.nextInt(12);
      }

      return valid;
   }
}
