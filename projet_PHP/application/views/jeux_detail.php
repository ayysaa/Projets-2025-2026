<div class="card">
    <h2><?php echo html_escape($game['name']); ?> (<?php echo html_escape($game['releaseYear']); ?>)</h2>
    <p style="font-size: 16px; color: #555;"><em><?php echo html_escape($game['shortDescription']); ?></em></p>

    <p><strong>Prix :</strong> <?php echo html_escape($game['price']); ?> €</p>
    <p><strong>Note Metacritic :</strong> <?php echo html_escape($game['metacritic'] ?? 'Non noté'); ?> / 100</p>

    <div class="flex-container" style="margin-top: 25px;">
        <div class="flex-child">
            <h3>📁 Catégories</h3>
            <ul>
                <?php if(!empty($categories)): ?>
                    <?php foreach($categories as $cat): ?>
                        <li><a href="<?php echo base_url('jeux/categorie/'.$cat['id']); ?>"><?php echo html_escape($cat['description']); ?></a></li>
                    <?php endforeach; ?>
                <?php else: ?>
                    <li style="color: #7f8c8d;">Aucune catégorie associée.</li>
                <?php endif; ?>
            </ul>
        </div>

        <div class="flex-child">
            <h3>🎭 Genres</h3>
            <ul>
                <?php if(!empty($genres)): ?>
                    <?php foreach($genres as $genre): ?>
                        <li><a href="<?php echo base_url('jeux/genre/'.$genre['id']); ?>"><?php echo html_escape($genre['description']); ?></a></li>
                    <?php endforeach; ?>
                <?php else: ?>
                    <li style="color: #7f8c8d;">Aucun genre associé.</li>
                <?php endif; ?>
            </ul>
        </div>
    </div>

    <div style="margin-top: 30px; border-top: 1px solid var(--border-color); padding-top: 20px;">
        <a href="<?php echo base_url('jeux'); ?>" class="btn btn-secondary">← Retour</a>
        <a href="<?php echo base_url('jeux/editer/'.$game['id']); ?>" class="btn btn-primary" style="background-color: #d35400;">Modifier la fiche</a>
    </div>
</div>