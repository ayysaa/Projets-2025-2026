<div class="card">
    <h2>Catégorie : <?php echo html_escape($category['description']); ?></h2>
    <h3>Jeux associés</h3>

    <table>
        <thead>
            <tr>
                <th>Titre</th>
                <th>Année</th>
                <th style="text-align: right;">Action</th>
            </tr>
        </thead>
        <tbody>
            <?php if (!empty($games)): ?>
                <?php foreach ($games as $g): ?>
                <tr>
                    <td><strong><?php echo html_escape($g['name']); ?></strong></td>
                    <td><?php echo html_escape($g['releaseYear']); ?></td>
                    <td style="text-align: right;">
                        <a href="<?php echo base_url('jeux/voir/'.$g['id']); ?>" class="btn btn-secondary" style="padding: 4px 8px; font-size: 13px;">Voir la fiche</a>
                    </td>
                </tr>
                <?php endforeach; ?>
            <?php else: ?>
                <tr>
                    <td colspan="3" style="text-align: center; color: #7f8c8d;">Aucun jeu dans cette catégorie.</td>
                </tr>
            <?php endif; ?>
        </tbody>
    </table>
    
    <p style="margin-top: 20px;"><a href="<?php echo base_url('jeux/categories_genres'); ?>" class="btn btn-secondary">← Retour aux catégories</a></p>
</div>