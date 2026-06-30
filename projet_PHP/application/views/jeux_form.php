<div class="card">
    <h2><?php echo $title; ?></h2>
    
    <?php echo validation_errors('<div style="color: var(--danger); margin-bottom: 15px; font-weight: bold;">', '</div>'); ?>

    <?php echo form_open(''); ?>
        <div class="form-group">
            <label for="name">Nom du jeu</label>
            <input type="text" name="name" id="name" class="form-control" value="<?php echo html_escape(set_value('name', $game['name'] ?? '')); ?>">
        </div>

        <div class="form-group">
            <label for="releaseYear">Année de sortie</label>
            <input type="number" name="releaseYear" id="releaseYear" class="form-control" value="<?php echo html_escape(set_value('releaseYear', $game['releaseYear'] ?? '')); ?>">
        </div>

        <div class="form-group">
            <label for="shortDescription">Description courte</label>
            <textarea name="shortDescription" id="shortDescription" class="form-control" rows="4"><?php echo html_escape(set_value('shortDescription', $game['shortDescription'] ?? '')); ?></textarea>
        </div>

        <div class="form-group">
            <label for="price">Prix (€)</label>
            <input type="text" name="price" id="price" class="form-control" value="<?php echo html_escape(set_value('price', $game['price'] ?? '')); ?>">
        </div>

        <div class="form-group">
            <label for="metacritic">Note Metacritic (Optionnel)</label>
            <input type="number" name="metacritic" id="metacritic" class="form-control" min="0" max="100" value="<?php echo html_escape(set_value('metacritic', $game['metacritic'] ?? '')); ?>">
        </div>

        <div style="margin-top: 20px;">
            <button type="submit" class="btn btn-primary">Enregistrer</button>
            <a href="<?php echo site_url('jeux'); ?>" class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</div>