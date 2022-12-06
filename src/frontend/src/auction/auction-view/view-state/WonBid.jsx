import React, { useCallback, useContext, useState } from 'react';
import { UserContext } from '../../../user.context';
import {
    Button,
    Content,
    Dialog,
    Flex,
    Heading,
    DialogTrigger,
    NumberField,
} from '@adobe/react-spectrum';
import { usePostWithToast } from '../../../http-query/use-post-with-toast';
import { StarRating } from '../../../star-rating/StarRating';

export const WonBid = ({ auction, refresh, ...otherProps }) => {
    const [rating, setRating] = useState(auction?.seller?.yourRating || 0);

    const isValid = useCallback(() => rating > 0, [rating]);

    const updateRating = usePostWithToast(
        `/auctions/${auction.id}/updateRating`,
        null,
        [auction],
        { message: 'Rating updated!' },
        { message: 'Failed to update rating!' },
        () => refresh(),
    );

    return (
        <DialogTrigger type="popover">
            <Button alignSelf="center" variant="cta" {...otherProps}>
                {rating > 0 ? 'Update Rating' : 'Rate Seller'}
            </Button>
            <Dialog size="M">
                <Heading>Rate {auction?.seller?.alias}</Heading>
                <Content>
                    <Flex
                        direction="rows"
                        justifyContent="space-between"
                        alignItems="end"
                        marginTop="size-100"
                        gap="size-50"
                    >
                        <StarRating rating={rating} onPress={setRating} />
                        <Button
                            width="size-2000"
                            variant="cta"
                            onPress={() =>
                                updateRating({ rating, auctionId: auction.id })
                            }
                            isDisabled={!isValid()}
                        >
                            Rate
                        </Button>
                    </Flex>
                </Content>
            </Dialog>
        </DialogTrigger>
    );
};
