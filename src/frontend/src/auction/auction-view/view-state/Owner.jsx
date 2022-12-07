import React from 'react';
import { Button, Flex } from '@adobe/react-spectrum';
import { useNavigate } from 'react-router-dom';

export const Owner = ({ auctionId, hasWinner, ...otherProps }) => {
    const navigate = useNavigate();

    return (
        <Flex {...otherProps}>
            <Button
                alignSelf="center"
                variant="cta"
                onPress={() => navigate(`/auction/${auctionId}/edit`)}
                {...otherProps}
            >
                {hasWinner ? 'View More Details' : 'Edit Listing'}
            </Button>
        </Flex>
    );
};
